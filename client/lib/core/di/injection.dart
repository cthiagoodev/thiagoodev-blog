import 'package:blog/core/http/http_client.dart';
import 'package:blog/data/clients/dio/base_options.dart';
import 'package:blog/data/clients/dio/dio_client.dart';
import 'package:blog/data/repositories/publications_remote_repository.dart';
import 'package:blog/domain/repositories/publication_repository.dart';
import 'package:blog/domain/usecases/get_current_week_publications_usecase.dart';
import 'package:blog/domain/usecases/get_featured_publication_usecase.dart';
import 'package:blog/domain/usecases/get_publication_by_uuid_usecase.dart';
import 'package:blog/domain/usecases/get_publications_usecase.dart';
import 'package:dio/dio.dart';
import 'package:get_it/get_it.dart';

final GetIt injection = GetIt.instance;

void setupInjection() {
  injection.registerLazySingleton<HttpClient>(
      () => DioClient(Dio(ServerBaseOptions.options)));

  injection.registerFactory<PublicationRepository>(
      () => PublicationsRemoteRepository(injection()));

  injection.registerFactory<GetPublicationsUseCase>(
      () => GetPublicationsUseCase(injection()));

  injection.registerFactory<GetPublicationByUuidUseCase>(
      () => GetPublicationByUuidUseCase(injection()));

  injection.registerFactory<GetFeaturedPublicationUseCase>(
      () => GetFeaturedPublicationUseCase(injection()));

  injection.registerFactory<GetCurrentWeekPublicationsUseCase>(
      () => GetCurrentWeekPublicationsUseCase(injection()));
}