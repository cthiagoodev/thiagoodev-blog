import 'package:blog/data/mappers/talk_mapper.dart';
import 'package:blog/data/models/publication_api_model.dart';
import 'package:blog/domain/models/publication.dart';

extension PublicationMapper on PublicationApiModel {
  Publication toEntity() {
    return Publication(
      uuid: uuid,
      title: title ?? 'Publicação sem título',
      slug: slug ?? '',
      description: description ?? '',
      text: text,
      viewsCount: viewsCount ?? 0,
      image: image,
      tags: tags?.where((t) => t.isNotEmpty).cast<String>().toList() ?? [],
      talks: talks?.map((e) => e.toEntity()).toList() ?? [],
      createdAt: createdAt != null ? DateTime.parse(createdAt!) : DateTime(0),
      updatedAt: updatedAt != null ? DateTime.tryParse(updatedAt!) : null,
    );
  }
}