final class Talk {
  final String? uuid;
  final String externalId;
  final String title;
  final DateTime? createdAt;
  final DateTime? updatedAt;

  const Talk({
    this.uuid,
    required this.externalId,
    required this.title,
    this.createdAt,
    this.updatedAt,
  });

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    return other is Talk && other.externalId == externalId;
  }

  @override
  int get hashCode => externalId.hashCode;
}