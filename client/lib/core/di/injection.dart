import 'package:blog/core/http/http_client.dart';
import 'package:blog/data/clients/dio/base_options.dart';
import 'package:blog/data/clients/dio/dio_client.dart';
import 'package:dio/dio.dart';
import 'package:get_it/get_it.dart';

final GetIt injection = GetIt.instance;

void setupInjection() {
  injection.registerLazySingleton<HttpClient>(
          () => DioClient(Dio(ServerBaseOptions.options)));
}