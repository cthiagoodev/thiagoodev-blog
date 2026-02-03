import 'package:json_annotation/json_annotation.dart';
import 'talk_api_model.dart';

part 'publication_api_model.g.dart';

@JsonSerializable()
class PublicationApiModel {
  final String? uuid;
  final String? title;
  final String? slug;
  final String? description;
  final String? text;
  final int? viewsCount;
  final String? image;
  final List<String>? tags;
  final List<TalkApiModel>? talks;
  final String? createdAt;
  final String? updatedAt;
  final String? deletedAt;

  PublicationApiModel({
    this.uuid,
    this.title,
    this.slug,
    this.description,
    this.text,
    this.viewsCount,
    this.image,
    this.tags,
    this.talks,
    this.createdAt,
    this.updatedAt,
    this.deletedAt,
  });

  factory PublicationApiModel.fromJson(Map<String, dynamic> json) => _$PublicationApiModelFromJson(json);

  Map<String, dynamic> toJson() => _$PublicationApiModelToJson(this);
}
