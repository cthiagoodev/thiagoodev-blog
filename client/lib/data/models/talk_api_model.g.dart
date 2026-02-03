// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'talk_api_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

TalkApiModel _$TalkApiModelFromJson(Map<String, dynamic> json) => TalkApiModel(
  uuid: json['uuid'] as String?,
  externalId: json['external_id'] as String?,
  title: json['title'] as String?,
  createdAt: json['created_at'] as String?,
  updatedAt: json['updated_at'] as String?,
);

Map<String, dynamic> _$TalkApiModelToJson(TalkApiModel instance) =>
    <String, dynamic>{
      'uuid': instance.uuid,
      'external_id': instance.externalId,
      'title': instance.title,
      'created_at': instance.createdAt,
      'updated_at': instance.updatedAt,
    };
