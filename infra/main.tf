resource "aws_cloudwatch_log_group" "ecs_log_group" {
  name              = "/ecs/api-processamento"
  retention_in_days = 7
}

resource "aws_ecs_task_definition" "api_task" {
  family                   = "api-processamento-task"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = "256"
  memory                   = "512"

  container_definitions = jsonencode([
    {
      name         = "api-container-processamento"
      image        = var.ecr_image
      essential    = true
      portMappings = [
        {
          containerPort = 8080
          protocol      = "tcp"
        }
      ]
      environment = [
        {
          name  = "AWS_ACCESS_KEY_ID"
          value = var.aws_access_key_id
        },
        {
          name  = "AWS_SECRET_ACCESS_KEY"
          value = var.aws_secret_access_key
        }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options   = {
          awslogs-group         = "/ecs/api-processamento"
          awslogs-region        = var.aws_region
          awslogs-stream-prefix = "ecs"
        }
      }
    }
  ])

  execution_role_arn = var.lab_role
}

resource "aws_security_group" "ecs_service_sg" {
  name   = "ecs-processamento-service-sg"
  vpc_id = var.vpc_id

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "alb_sg" {
  name   = "fiap-x-alb-sg"
  vpc_id = var.vpc_id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_lb" "api_alb" {
  name                       = "api-processamento-alb"
  internal                   = false
  load_balancer_type         = "application"
  security_groups            = [aws_security_group.alb_sg.id]
  subnets                    = var.subnet_ids
  enable_deletion_protection = false

  enable_cross_zone_load_balancing = true
}

resource "aws_lb_target_group" "api_target_group" {
  name        = "api-processamento-target-group"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "ip"

  health_check {
    path                = "/health"
    interval            = 60
    timeout             = 10
    healthy_threshold   = 3
    unhealthy_threshold = 3
    matcher             = "200"
  }
}

resource "aws_lb_listener_rule" "processamento_rule" {
  listener_arn = aws_lb.api_alb.arn
  priority     = 30

  condition {
    path_pattern {
      values = ["/api/v1/processamento*"]
    }
  }

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.api_target_group.arn
  }
}

resource "aws_ecs_service" "api_service" {
  name            = "api-processamento-service"
  cluster         = var.ecs_cluster_arn
  task_definition = aws_ecs_task_definition.api_task.arn
  launch_type     = "FARGATE"
  desired_count   = 1

  network_configuration {
    subnets          = var.subnet_ids
    security_groups  = [aws_security_group.ecs_service_sg.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.api_target_group.arn
    container_name   = "api-container-processamento"
    container_port   = 8080
  }
}

resource "aws_db_subnet_group" "fiapx_subnet_group" {
  name       = "fiapx-subnet-group"
  subnet_ids = var.subnet_ids
}

resource "aws_security_group" "fiapx_db_sg" {
  name        = "fiapx-db-sg"
  description = "Acesso ao RDS PostgreSQL"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_db_instance" "fiapx_db_produto" {
  db_name             = "fiapx_db"
  identifier          = "fiapx-db"
  allocated_storage   = 20
  engine              = "mysql"
  engine_version      = "8.0.39"
  instance_class      = "db.t3.micro"
  username            = "root"
  password            = "root1234"
  skip_final_snapshot = true

  vpc_security_group_ids = [aws_security_group.fiapx_db_sg.id]
  db_subnet_group_name   = aws_db_subnet_group.fiapx_subnet_group.name
}

resource "aws_api_gateway_resource" "processamento_resource" {
  rest_api_id = var.api_gateway_id
  parent_id   = var.api_gateway_root_resource_id
  path_part   = "processamento"
}

resource "aws_api_gateway_method" "processamento_get_method" {
  rest_api_id   = var.api_gateway_id
  resource_id   = aws_api_gateway_resource.processamento_resource.id
  http_method   = "GET"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "processamento_get_integration" {
  rest_api_id             = var.api_gateway_id
  resource_id             = aws_api_gateway_resource.processamento_resource.id
  http_method             = aws_api_gateway_method.processamento_get_method.http_method
  integration_http_method = "GET"
  type                    = "HTTP_PROXY"
  uri                     = "http://${aws_lb.api_alb.dns_name}/api/v1/processamento"
}

resource "aws_api_gateway_deployment" "api_deployment" {
  rest_api_id = var.api_gateway_id

  depends_on = [
    aws_api_gateway_integration.processamento_get_integration,
    aws_api_gateway_method.processamento_get_method
  ]
}

resource "aws_api_gateway_stage" "api_stage" {
  stage_name    = "prod"
  rest_api_id   = var.api_gateway_id
  deployment_id = aws_api_gateway_deployment.api_deployment.id

  depends_on = [
    aws_api_gateway_deployment.api_deployment
  ]
}