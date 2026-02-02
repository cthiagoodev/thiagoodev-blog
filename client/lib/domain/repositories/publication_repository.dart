import 'package:blog/domain/models/paginated_result.dart';
import 'package:blog/domain/models/publication.dart';

abstract class PublicationRepository {
  Future<PaginatedResult<Publication>> getAll({int page = 0, int size = 10});
  Future<Publication> getByUuid(String uuid);
  Future<Publication> getFeatured();
  Future<List<Publication>> getOnCurrentWeek();
}