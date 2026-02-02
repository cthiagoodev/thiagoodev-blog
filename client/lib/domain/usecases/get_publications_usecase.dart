import 'package:blog/domain/models/paginated_result.dart';
import 'package:blog/domain/models/publication.dart';
import 'package:blog/domain/repositories/publication_repository.dart';

final class GetPublicationsUseCase {
  final PublicationRepository _repository;

  GetPublicationsUseCase(this._repository);

  Future<PaginatedResult<Publication>> call({int page = 0, int size = 10}) async {
    return _repository.getAll(page: page, size: size);
  }
}