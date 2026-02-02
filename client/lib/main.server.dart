library;

import 'package:blog/core/di/injection.dart';
import 'package:jaspr/dom.dart';
import 'package:jaspr/server.dart';
import 'app.dart';
import 'core/constants/theme.dart';
import 'main.server.options.dart';

void main() {
  Jaspr.initializeApp(
    options: defaultServerOptions,
  );

  setupInjection();

  runApp(Document(
    title: 'thiagoodev | Blog',
    styles: [
      css.import('https://fonts.googleapis.com/css?family=Plus%20Jakarta%20Sans'),
      css('html, body').styles(
        padding: .zero,
        margin: .zero,
        color: AppColors.neutral,
        fontFamily: AppTheme.fontFamily,
        backgroundColor: AppColors.background,
      ),
    ],
    body: App(),
  ));
}
