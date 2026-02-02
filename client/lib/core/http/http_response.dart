final class HttpResponse<T> {
  final T? data;
  final int? statusCode;
  final Map<String, dynamic> headers;

  const HttpResponse({
    required this.data,
    required this.statusCode,
    required this.headers,
  });
}