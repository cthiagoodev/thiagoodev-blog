import 'package:blog/core/http/http_exception.dart';
import 'package:blog/core/http/http_response.dart';
import 'package:dio/dio.dart';

extension DioResponseExtension<T> on Response<T> {
  HttpResponse<T> toHttpResponse() {
    return HttpResponse(
      data: data,
      statusCode: statusCode,
      headers: headers.map,
    );
  }
}

extension DioErrorExtension on DioException {
  HttpFailure toHttpFailure() {
    if (type == DioExceptionType.connectionTimeout ||
        type == DioExceptionType.receiveTimeout ||
        type == DioExceptionType.connectionError) {
      return const HttpFailure(
        message: 'Falha na conexão. Verifique sua internet ou tente novamente.',
        statusCode: 0,
      );
    }

    if (response != null) {
      final data = response?.data;

      if (data is Map<String, dynamic>) {
        return HttpFailure(
          message: data['message'] ?? 'Erro desconhecido no servidor.',
          statusCode: data['status'] ?? response?.statusCode ?? 500,
        );
      }
    }

    return HttpFailure(
      message: message ?? 'Ocorreu um erro inesperado.',
      statusCode: response?.statusCode ?? 500,
    );
  }
}
