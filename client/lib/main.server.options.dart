// dart format off
// ignore_for_file: type=lint

// GENERATED FILE, DO NOT MODIFY
// Generated with jaspr_builder

import 'package:jaspr/server.dart';
import 'package:blog/presentation/global_components/app_card.dart' as _app_card;
import 'package:blog/presentation/global_components/app_container.dart'
    as _app_container;
import 'package:blog/presentation/global_components/badge.dart' as _badge;
import 'package:blog/presentation/global_components/buttons.dart' as _buttons;
import 'package:blog/presentation/global_components/header.dart' as _header;
import 'package:blog/presentation/global_components/nav.dart' as _nav;
import 'package:blog/presentation/global_components/section_title.dart'
    as _section_title;
import 'package:blog/presentation/pages/home/home_screen.dart' as _home_screen;
import 'package:blog/presentation/pages/posts/components/featured_post.dart'
    as _featured_post;
import 'package:blog/presentation/pages/posts/components/post_card.dart'
    as _post_card;
import 'package:blog/presentation/pages/posts/components/weekly_carousel.dart'
    as _weekly_carousel;
import 'package:blog/app.dart' as _app;

/// Default [ServerOptions] for use with your Jaspr project.
///
/// Use this to initialize Jaspr **before** calling [runApp].
///
/// Example:
/// ```dart
/// import 'main.server.options.dart';
///
/// void main() {
///   Jaspr.initializeApp(
///     options: defaultServerOptions,
///   );
///
///   runApp(...);
/// }
/// ```
ServerOptions get defaultServerOptions => ServerOptions(
  clientId: 'main.client.dart.js',

  styles: () => [
    ..._app_card.AppCard.styles,
    ..._app_container.AppContainer.styles,
    ..._badge.Badge.styles,
    ..._buttons.Button.styles,
    ..._buttons.LinkButton.styles,
    ..._header.Header.styles,
    ..._nav.Nav.styles,
    ..._section_title.SectionTitle.styles,
    ..._home_screen.HomeScreen.styles,
    ..._featured_post.FeaturedPost.styles,
    ..._post_card.PostCard.styles,
    ..._weekly_carousel.WeeklyCarousel.styles,
    ..._app.App.styles,
  ],
);
