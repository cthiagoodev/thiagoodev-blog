// dart format off
// ignore_for_file: type=lint

// GENERATED FILE, DO NOT MODIFY
// Generated with jaspr_builder

import 'package:jaspr/server.dart';
import 'package:blog/modules/components/app_card.dart' as _app_card;
import 'package:blog/modules/components/app_container.dart' as _app_container;
import 'package:blog/modules/components/badge.dart' as _badge;
import 'package:blog/modules/components/buttons.dart' as _buttons;
import 'package:blog/modules/components/section_title.dart' as _section_title;
import 'package:blog/modules/header/header.dart' as _header;
import 'package:blog/modules/header/nav.dart' as _nav;
import 'package:blog/modules/home/home_screen.dart' as _home_screen;
import 'package:blog/modules/posts/components/featured_post.dart'
    as _featured_post;
import 'package:blog/modules/posts/components/post_card.dart' as _post_card;
import 'package:blog/modules/posts/components/weekly_carousel.dart'
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
    ..._section_title.SectionTitle.styles,
    ..._header.Header.styles,
    ..._nav.Nav.styles,
    ..._home_screen.HomeScreen.styles,
    ..._featured_post.FeaturedPost.styles,
    ..._post_card.PostCard.styles,
    ..._weekly_carousel.WeeklyCarousel.styles,
    ..._app.App.styles,
  ],
);
