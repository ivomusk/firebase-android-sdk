schema {
  table("zwda6x9zyy") {
    val idField = field("myId", UUIDScalar.NonNull)
    field("string", StringScalar.NonNull)
    key(idField)
  }
}