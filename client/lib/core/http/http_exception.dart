final class HttpFailure implements Exception {
  final String message;
  final int statusCode;

  const HttpFailure({
    required this.message,
    required this.statusCode,
  });

  @override
  String toString() => 'HttpFailure(status: $statusCode, message: $message)';
}