// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'paginated_response_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

PaginatedResponseModel<T> _$PaginatedResponseModelFromJson<T>(
  Map<String, dynamic> json,
  T Function(Object? json) fromJsonT,
) => PaginatedResponseModel<T>(
  content: (json['content'] as List<dynamic>?)?.map(fromJsonT).toList(),
  meta: json['page'] == null
      ? null
      : PageMetadataApiModel.fromJson(json['page'] as Map<String, dynamic>),
);

Map<String, dynamic> _$PaginatedResponseModelToJson<T>(
  PaginatedResponseModel<T> instance,
  Object? Function(T value) toJsonT,
) => <String, dynamic>{
  'content': instance.content?.map(toJsonT).toList(),
  'page': instance.meta,
};

PageMetadataApiModel _$PageMetadataApiModelFromJson(
  Map<String, dynamic> json,
) => PageMetadataApiModel(
  size: (json['size'] as num?)?.toInt(),
  number: (json['number'] as num?)?.toInt(),
  totalElements: (json['total_elements'] as num?)?.toInt(),
  totalPages: (json['total_pages'] as num?)?.toInt(),
);

Map<String, dynamic> _$PageMetadataApiModelToJson(
  PageMetadataApiModel instance,
) => <String, dynamic>{
  'size': instance.size,
  'number': instance.number,
  'total_elements': instance.totalElements,
  'total_pages': instance.totalPages,
};
