import 'package:blog/data/models/paginated_response_model.dart';
import 'package:blog/domain/models/paginated_result.dart';

extension PaginatedResponseMapper<T> on PaginatedResponseModel<T> {
  PaginatedResult<E> toEntity<E>(E Function(T apiModel) itemMapper) {
    return PaginatedResult(
      items: content?.map((item) => itemMapper(item)).toList() ?? [],
      totalItems: meta?.totalElements ?? 0,
      totalPages: meta?.totalPages ?? 0,
      currentPage: meta?.number ?? 0,
    );
  }
}