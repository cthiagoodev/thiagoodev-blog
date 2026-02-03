import 'package:blog/data/models/talk_api_model.dart';
import 'package:blog/domain/models/talk.dart';

extension TalkMapper on TalkApiModel {
  Talk toEntity() {
    return Talk(
      uuid: uuid,
      externalId: externalId ?? '',
      title: title ?? 'Sem título',
      createdAt: createdAt != null ? DateTime.tryParse(createdAt!) : null,
      updatedAt: updatedAt != null ? DateTime.tryParse(updatedAt!) : null,
    );
  }
}