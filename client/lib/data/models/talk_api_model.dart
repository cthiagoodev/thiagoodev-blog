import 'package:json_annotation/json_annotation.dart';

part 'talk_api_model.g.dart';

@JsonSerializable()
class TalkApiModel {
  final String? uuid;
  final String? externalId;
  final String? title;
  final String? createdAt;
  final String? updatedAt;

  TalkApiModel({
    this.uuid,
    this.externalId,
    this.title,
    this.createdAt,
    this.updatedAt,
  });

  factory TalkApiModel.fromJson(Map<String, dynamic> json) => _$TalkApiModelFromJson(json);

  Map<String, dynamic> toJson() => _$TalkApiModelToJson(this);
}
