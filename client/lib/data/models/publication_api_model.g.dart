// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'publication_api_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

PublicationApiModel _$PublicationApiModelFromJson(Map<String, dynamic> json) =>
    PublicationApiModel(
      uuid: json['uuid'] as String?,
      title: json['title'] as String?,
      slug: json['slug'] as String?,
      description: json['description'] as String?,
      text: json['text'] as String?,
      viewsCount: (json['views_count'] as num?)?.toInt(),
      image: json['image'] as String?,
      tags: (json['tags'] as List<dynamic>?)?.map((e) => e as String).toList(),
      talks: (json['talks'] as List<dynamic>?)
          ?.map((e) => TalkApiModel.fromJson(e as Map<String, dynamic>))
          .toList(),
      createdAt: json['created_at'] as String?,
      updatedAt: json['updated_at'] as String?,
      deletedAt: json['deleted_at'] as String?,
    );

Map<String, dynamic> _$PublicationApiModelToJson(
  PublicationApiModel instance,
) => <String, dynamic>{
  'uuid': instance.uuid,
  'title': instance.title,
  'slug': instance.slug,
  'description': instance.description,
  'text': instance.text,
  'views_count': instance.viewsCount,
  'image': instance.image,
  'tags': instance.tags,
  'talks': instance.talks,
  'created_at': instance.createdAt,
  'updated_at': instance.updatedAt,
  'deleted_at': instance.deletedAt,
};
