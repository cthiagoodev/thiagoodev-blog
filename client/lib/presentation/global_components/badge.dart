import 'package:blog/core/constants/theme.dart';
import 'package:jaspr/dom.dart';
import 'package:jaspr/jaspr.dart';

enum BadgeVariant { primary, neutral }

final class Badge extends StatelessComponent {
  final String label;
  final BadgeVariant variant;

  const Badge({required this.label, this.variant = .primary});

  @override
  Component build(BuildContext context) {
    return span(classes: 'badge badge-${variant.name}', [.text(label)]);
  }

  @css
  static List<StyleRule> get styles => [
    css('.badge').styles(
      display: Display.inlineFlex,
      padding: Padding.symmetric(vertical: 0.35.rem, horizontal: 0.75.rem),
      radius: BorderRadius.circular(2.rem),
      fontSize: 0.75.rem,
      fontWeight: FontWeight.w700,
      textTransform: .upperCase,
      letterSpacing: 0.05.em,
    ),
    css('.badge-primary').styles(
      color: AppColors.primaryDeep,
      backgroundColor: AppColors.primaryLight,
    ),
    css('.badge-neutral').styles(
      color: AppColors.neutral,
      backgroundColor: AppColors.surface,
    ),
  ];
}