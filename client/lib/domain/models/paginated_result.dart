final class PaginatedResult<T> {
  final List<T> items;
  final int totalItems;
  final int totalPages;
  final int currentPage;

  const PaginatedResult({
    required this.items,
    required this.totalItems,
    required this.totalPages,
    required this.currentPage,
  });
}