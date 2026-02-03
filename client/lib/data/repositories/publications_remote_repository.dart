import 'package:blog/core/http/http_client.dart';
import 'package:blog/core/http/http_response.dart';
import 'package:blog/data/mappers/paginated_response_mapper.dart';
import 'package:blog/data/mappers/publication_mapper.dart';
import 'package:blog/data/models/paginated_response_model.dart';
import 'package:blog/data/models/publication_api_model.dart';
import 'package:blog/domain/models/paginated_result.dart';
import 'package:blog/domain/models/publication.dart';
import 'package:blog/domain/repositories/publication_repository.dart';

final class PublicationsRemoteRepository implements PublicationRepository {
  final HttpClient _client;

  const PublicationsRemoteRepository(this._client);

  static const String basePath = "/api/publications";

  @override
  Future<PaginatedResult<Publication>> getAll({int page = 0, int size = 10}) async {
    final HttpResponse response = await _client.get(
      basePath,
      queryParameters: {
        'page': page,
        'size': size,
      },
    );

    if (response.data == null || response.data is! Map) {
      throw const FormatException(
        "Resposta inválida do servidor: Esperado um objeto JSON paginado.",
      );
    }

    final model = PaginatedResponseModel<PublicationApiModel>.fromJson(
      response.data as Map<String, dynamic>,
      (json) => PublicationApiModel.fromJson(json as Map<String, dynamic>),
    );

    return model.toEntity(
      (apiModel) => apiModel.toEntity(),
    );
  }

  @override
  Future<Publication> getByUuid(String uuid) async {
    final HttpResponse response = await _client.get('$basePath/$uuid');

    if (response.data == null || response.data is! Map) {
      throw FormatException(
        "Resposta inválida do servidor para UUID $uuid: Esperado um objeto JSON.",
      );
    }

    return PublicationApiModel.fromJson(response.data as Map<String, dynamic>).toEntity();
  }

  @override
  Future<Publication> getFeatured() async {
    final HttpResponse response = await _client.get('$basePath/featured');

    if (response.data == null || response.data is! Map) {
      throw const FormatException(
        "Resposta inválida do servidor para Destaque: Esperado um objeto JSON.",
      );
    }

    return PublicationApiModel.fromJson(response.data as Map<String, dynamic>).toEntity();
  }

  @override
  Future<List<Publication>> getOnCurrentWeek() async {
    final HttpResponse response = await _client.get('$basePath/on-current-week');

    if (response.data == null || response.data is! List) {
      throw const FormatException(
        "Resposta inválida do servidor para Semana: Esperada uma lista JSON.",
      );
    }

    return (response.data as List)
        .map((e) => PublicationApiModel.fromJson(e as Map<String, dynamic>).toEntity())
        .toList();
  }
}
