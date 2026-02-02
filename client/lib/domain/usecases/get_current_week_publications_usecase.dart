import 'package:blog/domain/models/publication.dart';
import 'package:blog/domain/repositories/publication_repository.dart';

final class GetCurrentWeekPublicationsUseCase {
  final PublicationRepository _repository;

  GetCurrentWeekPublicationsUseCase(this._repository);

  Future<List<Publication>> call() async {
    return _repository.getOnCurrentWeek();
  }
}