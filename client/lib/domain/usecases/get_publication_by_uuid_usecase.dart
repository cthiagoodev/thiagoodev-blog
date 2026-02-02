import 'package:blog/domain/models/publication.dart';
import 'package:blog/domain/repositories/publication_repository.dart';

final class GetPublicationByUuidUseCase {
  final PublicationRepository _repository;

  GetPublicationByUuidUseCase(this._repository);

  Future<Publication> call(String uuid) async {
    return _repository.getByUuid(uuid);
  }
}