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

  container_definitions = jsonencode([{
    name      = "api-container-processamento"
    image     = var.ecr_image
    essential = true
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
      options = {
        awslogs-group         = "/ecs/api-processamento"
        awslogs-region        = var.aws_region
        awslogs-stream-prefix = "ecs"
      }
    }
  }])

  execution_role_arn = var.lab_role
}

resource "aws_security_group" "ecs_service_sg" {
  name   = "ecs-processamento-service-sg"
  vpc_id = var.vpc_id

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
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
}