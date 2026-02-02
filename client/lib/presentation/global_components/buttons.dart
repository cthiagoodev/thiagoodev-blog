import 'package:blog/core/constants/theme.dart';
import 'package:jaspr/dom.dart';
import 'package:jaspr/jaspr.dart';
import 'package:jaspr_lucide/jaspr_lucide.dart' as jl;

enum _ButtonVariant { primary, secondary, outline, ghost }

class Button extends StatelessComponent {
  final String label;
  final VoidCallback? onPressed;
  final String? href;
  final _ButtonVariant _variant;

  const Button.primary({
    required this.label,
    this.onPressed,
    this.href,
    super.key,
  }) : _variant = _ButtonVariant.primary;

  const Button.secondary({
    required this.label,
    this.onPressed,
    this.href,
    super.key,
  }) : _variant = _ButtonVariant.secondary;

  const Button.outline({
    required this.label,
    this.onPressed,
    this.href,
    super.key,
  }) : _variant = _ButtonVariant.outline;

  const Button.ghost({
    required this.label,
    this.onPressed,
    this.href,
    super.key,
  }) : _variant = _ButtonVariant.ghost;

  @override
  Component build(BuildContext context) {
    final classes = 'btn btn-${_variant.name}';

    if (href != null) {
      return a(href: href!, classes: classes, [.text(label)]);
    }

    return button(
      classes: classes,
      events: onPressed != null ? {'click': (e) => onPressed!()} : {},
      [.text(label)],
    );
  }

  @css
  static List<StyleRule> get styles => [
    css('.btn').styles(
      display: Display.inlineFlex,
      padding: Padding.symmetric(vertical: 0.8.rem, horizontal: 1.3.rem),
      border: Border.all(width: 1.5.px, color: Colors.transparent),
      radius: BorderRadius.circular(0.6.rem),
      outline: Outline.unset,
      cursor: Cursor.pointer,
      transition: const Transition('all', duration: Duration(milliseconds: 250)),
      justifyContent: JustifyContent.center,
      alignItems: AlignItems.center,
      fontFamily: AppTheme.fontFamily,
      fontSize: AppFontSizes.bodySm,
      fontWeight: FontWeight.w600,
      textDecoration: .none,
    ),

    css('.btn:hover').styles(
      shadow: BoxShadow(
        offsetX: 0.px,
        offsetY: 4.px,
        blur: 12.px,
        color: Color.rgba(0, 0, 0, 0.1),
      ),
      transform: Transform.scale(1.03),
    ),

    css('.btn:active').styles(
      transform: Transform.scale(0.97),
    ),

    css('.btn-primary').styles(
      color: Color('#FFFFFF'),
      backgroundColor: AppColors.primary,
    ),

    css('.btn-primary:hover').styles(
      backgroundColor: AppColors.primaryDark,
    ),

    css('.btn-secondary').styles(
      color: AppColors.foreground,
      backgroundColor: AppColors.surface,
    ),

    css('.btn-secondary:hover').styles(
      backgroundColor: AppColors.border,
    ),

    css('.btn-outline').styles(
      border: Border.all(width: 1.5.px, color: AppColors.primary),
      color: AppColors.primary,
      backgroundColor: Colors.transparent,
    ),

    css('.btn-outline:hover').styles(
      color: Color('#FFFFFF'),
      backgroundColor: AppColors.primary,
    ),

    css('.btn-ghost').styles(
      color: AppColors.textSecondary,
      backgroundColor: Colors.transparent,
    ),

    css('.btn-ghost:hover').styles(
      shadow: .none,
      color: AppColors.primaryDeep,
      backgroundColor: AppColors.primaryLight,
    ),
  ];
}

class LinkButton extends StatelessComponent {
  final String label;
  final String? href;

  const LinkButton({required this.label, this.href});

  @override
  Component build(BuildContext context) {
    if (href != null) {
      return a(href: href!, classes: 'link-btn', [
        .text(label),
        jl.ArrowRight(width: 16.px, height: 16.px),
      ]);
    }

    return span(classes: 'link-btn', [
      .text(label),
      jl.ArrowRight(width: 16.px, height: 16.px),
    ]);
  }

  @css
  static List<StyleRule> get styles => [
    css('.link-btn').styles(
      display: Display.flex,
      alignItems: AlignItems.center,
      gap: Gap(column: 0.5.rem),
      color: AppColors.primary,
      fontSize: AppFontSizes.body,
      fontWeight: FontWeight.w600,
      textDecoration: TextDecoration.none,
      cursor: Cursor.pointer,
      transition: const Transition('gap', duration: Duration(milliseconds: 200)),
    ),
    css('.link-btn:hover').styles(
      textDecoration: TextDecoration(line: TextDecorationLine.underline),
      gap: Gap(column: 0.75.rem),
    ),
  ];
}
