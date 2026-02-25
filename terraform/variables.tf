############################################
# Kubernetes Configuration Variables
############################################

variable "namespace_dev" {
  description = "Development namespace name"
  type        = string
  default     = "development"
}

variable "namespace_prod" {
  description = "Production namespace name"
  type        = string
  default     = "production"
}

############################################
# Application Configuration
############################################

variable "app_name" {
  description = "Application name"
  type        = string
  default     = "nginx-app"
}

variable "app_image" {
  description = "Container image"
  type        = string
  default     = "nginx:latest"
}

variable "replica_count" {
  description = "Number of pod replicas"
  type        = number
  default     = 2
}

variable "container_port" {
  description = "Container exposed port"
  type        = number
  default     = 80
}

############################################
# Service Configuration
############################################

variable "service_type" {
  description = "Kubernetes service type"
  type        = string
  default     = "NodePort"
}

variable "service_port" {
  description = "Service port"
  type        = number
  default     = 80
}