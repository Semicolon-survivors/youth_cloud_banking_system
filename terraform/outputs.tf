############################################
# Namespace Outputs
############################################

output "development_namespace" {
  description = "Development namespace name"
  value       = kubernetes_namespace.dev.metadata[0].name
}

output "production_namespace" {
  description = "Production namespace name"
  value       = kubernetes_namespace.prod.metadata[0].name
}

############################################
# Deployment Output
############################################

output "deployment_name" {
  description = "Kubernetes deployment name"
  value       = kubernetes_deployment.nginx.metadata[0].name
}

############################################
# Service Output
############################################

output "service_name" {
  description = "Kubernetes service name"
  value       = kubernetes_service.nginx_service.metadata[0].name
}

output "service_port" {
  description = "Service exposed port"
  value       = var.service_port
}