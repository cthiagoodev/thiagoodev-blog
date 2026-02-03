import 'package:json_annotation/json_annotation.dart';

part 'paginated_response_model.g.dart';

@JsonSerializable(genericArgumentFactories: true)
class PaginatedResponseModel<T> {
  final List<T>? content;
  @JsonKey(name: 'page')
  final PageMetadataApiModel? meta;

  PaginatedResponseModel({
    this.content,
    this.meta,
  });

  factory PaginatedResponseModel.fromJson(
    Map<String, dynamic> json,
    T Function(Object? json) fromJsonT,
  ) => _$PaginatedResponseModelFromJson(json, fromJsonT);
}

@JsonSerializable()
class PageMetadataApiModel {
  final int? size;
  final int? number;
  final int? totalElements;
  final int? totalPages;

  PageMetadataApiModel({
    this.size,
    this.number,
    this.totalElements,
    this.totalPages,
  });

  factory PageMetadataApiModel.fromJson(Map<String, dynamic> json) => _$PageMetadataApiModelFromJson(json);
}
