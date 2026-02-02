import 'talk.dart';

final class Publication {
  final String? uuid;
  final String title;
  final String slug;
  final String description;
  final String? text;
  final int viewsCount;
  final String? image;
  final List<String> tags;
  final List<Talk> talks;
  final DateTime createdAt;
  final DateTime? updatedAt;

  const Publication({
    this.uuid,
    required this.title,
    required this.slug,
    required this.description,
    this.text,
    this.viewsCount = 0,
    this.image,
    this.tags = const [],
    this.talks = const [],
    required this.createdAt,
    this.updatedAt,
  });

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    return other is Publication && other.slug == slug;
  }

  @override
  int get hashCode => slug.hashCode;
}