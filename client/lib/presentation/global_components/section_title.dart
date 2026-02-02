import 'package:blog/core/constants/theme.dart';
import 'package:jaspr/dom.dart';
import 'package:jaspr/jaspr.dart';

class SectionTitle extends StatelessComponent {
  final String title;
  final String? subtitle;

  const SectionTitle({required this.title, this.subtitle});

  @override
  Component build(BuildContext context) {
    return div(classes: 'section-header', [
      h2(classes: 'section-title', [.text(title)]),
      if (subtitle != null)
        p(classes: 'section-subtitle', [.text(subtitle!)]),
    ]);
  }

  @css
  static List<StyleRule> get styles => [
    css('.section-header').styles(
      display: Display.flex,
      margin: Margin.only(bottom: 2.rem),
      flexDirection: FlexDirection.column,
      gap: Gap(row: 0.5.rem),
    ),
    css('.section-title').styles(
      margin: Margin.zero,
      color: AppColors.foreground,
      fontSize: AppFontSizes.h2,
      fontWeight: FontWeight.w700,
      lineHeight: AppLineHeights.tight,
    ),
    css('.section-subtitle').styles(
      margin: Margin.zero,
      color: AppColors.neutral,
      fontSize: AppFontSizes.body,
    ),
    css('@media (max-width: 768px) .section-title').styles(
      raw: {'font-size': '1.75rem'},
    ),
  ];
}