resource "kubernetes_namespace" "banking" {
  metadata {
    name = "banking-system"
  }
}