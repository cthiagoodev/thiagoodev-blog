import 'package:blog/domain/models/publication.dart';
import 'package:blog/domain/repositories/publication_repository.dart';

final class GetFeaturedPublicationUseCase {
  final PublicationRepository _repository;

  GetFeaturedPublicationUseCase(this._repository);

  Future<Publication> call() async {
    return _repository.getFeatured();
  }
}