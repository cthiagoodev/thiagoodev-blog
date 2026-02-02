import 'package:blog/core/http/config.dart';
import 'package:dio/dio.dart';

abstract class ServerBaseOptions {
  static BaseOptions get options => BaseOptions(
    baseUrl: NetworkConfig.apiUrl,
    connectTimeout: Duration(seconds: 5),
    receiveTimeout: Duration(seconds: 3),
  );
}

