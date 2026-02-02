import 'dart:developer';
import 'package:blog/core/http/http_client.dart';
import 'package:blog/core/http/http_response.dart';
import 'package:blog/data/clients/dio/dio_extensions.dart';
import 'package:dio/dio.dart';

final class DioClient implements HttpClient {
  final Dio _dio;

  DioClient(this._dio) {
    _dio.options.headers['Content-Type'] = 'application/json';
    _dio.interceptors.add(
      LogInterceptor(
        requestBody: true,
        responseBody: true,
        logPrint: (obj) => log(obj.toString(), name: 'DIO'),
      ),
    );
  }

  @override
  Future<HttpResponse<T>> get<T>(
    String path, {
    Map<String, dynamic>? queryParameters,
    Map<String, dynamic>? headers,
  }) async {
    try {
      final response = await _dio.get<T>(
        path,
        queryParameters: queryParameters,
        options: Options(headers: headers),
      );
      return response.toHttpResponse();
    } on DioException catch (e) {
      throw e.toHttpFailure();
    }
  }

  @override
  Future<HttpResponse<T>> post<T>(
    String path, {
    Object? data,
    Map<String, dynamic>? queryParameters,
    Map<String, dynamic>? headers,
  }) async {
    try {
      final response = await _dio.post<T>(
        path,
        data: data,
        queryParameters: queryParameters,
        options: Options(headers: headers),
      );
      return response.toHttpResponse();
    } on DioException catch (e) {
      throw e.toHttpFailure();
    }
  }

  @override
  Future<HttpResponse<T>> put<T>(
    String path, {
    Object? data,
    Map<String, dynamic>? queryParameters,
    Map<String, dynamic>? headers,
  }) async {
    try {
      final response = await _dio.put<T>(
        path,
        data: data,
        queryParameters: queryParameters,
        options: Options(headers: headers),
      );
      return response.toHttpResponse();
    } on DioException catch (e) {
      throw e.toHttpFailure();
    }
  }

  @override
  Future<HttpResponse<T>> delete<T>(
    String path, {
    Object? data,
    Map<String, dynamic>? queryParameters,
    Map<String, dynamic>? headers,
  }) async {
    try {
      final response = await _dio.delete<T>(
        path,
        data: data,
        queryParameters: queryParameters,
        options: Options(headers: headers),
      );
      return response.toHttpResponse();
    } on DioException catch (e) {
      throw e.toHttpFailure();
    }
  }
}
