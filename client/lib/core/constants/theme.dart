import 'package:jaspr/dom.dart';

abstract class AppColors {
  static const background = Color('#000000');
  static const foreground = Color('#F9FAFA');
  static const primary = Color('#5842C3');
  static const primaryDark = Color('#251E40');
  static const primaryLight = Color('#E3B0FF');
  static const neutral = Color('#BDBDBD');
  static const accent = Color('#00D9FF');
  static const community = Color('#00E676');
}

abstract class AppSpacing {
  static final xs = 0.25.rem;
  static final sm = 0.5.rem;
  static final md = 1.0.rem;
  static final lg = 1.5.rem;
  static final xl = 2.0.rem;
  static final xl2 = 2.5.rem;
  static final xl3 = 4.0.rem;
}

abstract class AppTheme {
  static Map<String, String> get rawVariables => {
    '--color-background': AppColors.background.value,
    '--color-foreground': AppColors.foreground.value,
    '--color-primary': AppColors.primary.value,
    '--color-primary-dark': AppColors.primaryDark.value,
    '--color-primary-light': AppColors.primaryLight.value,
    '--color-neutral': AppColors.neutral.value,
    '--color-accent': AppColors.accent.value,
    '--color-community': AppColors.community.value,

    '--spacing-xs': AppSpacing.xs.value,
    '--spacing-sm': AppSpacing.sm.value,
    '--spacing-md': AppSpacing.md.value,
    '--spacing-lg': AppSpacing.lg.value,
    '--spacing-xl': AppSpacing.xl.value,
    '--spacing-2xl': AppSpacing.xl2.value,
    '--spacing-3xl': AppSpacing.xl3.value,

    '--container-md': '1200px',
    '--container-lg': '1400px',
    '--z-header': '1000',
  };
}