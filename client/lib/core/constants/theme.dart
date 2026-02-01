import 'package:jaspr/dom.dart';

abstract class AppColors {
  static const background = Color('#FBFBFA');
  static const foreground = Color('#121212');
  static const primary = Color('#059669');
  static const primaryDark = Color('#064E3B');
  static const primaryDeep = Color('#047857');
  static const primaryLight = Color('#ECFDF5');
  static const neutral = Color('#6B7280');
  static const textSecondary = Color('#374151');
  static const surface = Color('#F3F4F6');
  static const white = Color('#FFFFFF');
  static const border = Color('#E5E7EB');
}

abstract class AppFontSizes {
  static final display = 3.5.rem;
  static final h1 = 2.5.rem;
  static final h2 = 2.0.rem;
  static final h3 = 1.75.rem;
  static final h4 = 1.5.rem;
  static final bodyLg = 1.125.rem;
  static final body = 1.0.rem;
  static final bodySm = 0.875.rem;
  static final caption = 0.75.rem;
}

abstract class AppLineHeights {
  static final tight = 1.1.rem;
  static final snug = 1.3.rem;
  static final normal = 1.5.rem;
  static final relaxed = 1.75.rem;
}

abstract class AppShadows {
  static final card = BoxShadow(
    offsetX: 0.px,
    offsetY: 4.px,
    blur: 20.px,
    color: Color.rgba(0, 0, 0, 0.05),
  );

  static final cardHover = BoxShadow(
    offsetX: 0.px,
    offsetY: 10.px,
    blur: 25.px,
    color: Color.rgba(0, 0, 0, 0.08),
  );
}

abstract class AppRadius {
  static final sm = BorderRadius.circular(0.5.rem);
  static final md = BorderRadius.circular(1.0.rem);
  static final lg = BorderRadius.circular(1.5.rem);
  static final xl = BorderRadius.circular(2.0.rem);
}

abstract class AppTheme {
  static final containerLg = 1200.px;
  static final glassBlur = 12.px;
  static final fontFamily = FontFamily.list(
    [FontFamily('Plus Jakarta Sans'), FontFamilies.sansSerif],
  );
}