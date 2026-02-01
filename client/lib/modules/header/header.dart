import 'package:blog/core/constants/theme.dart';
import 'package:blog/modules/header/nav.dart';
import 'package:jaspr/dom.dart';
import 'package:jaspr/jaspr.dart';

final class Header extends StatelessComponent {
  @override
  Component build(BuildContext context) {
    return header([
      Nav(),
    ]);
  }

  @css
  static List<StyleRule> get styles => [
    css('header').styles(
      position: .sticky(top: 0.px),
      zIndex: ZIndex(1000),
      width: 100.percent,
      border: Border.only(bottom: BorderSide(width: 1.px, color: AppColors.border)),
      backdropFilter: Filter.blur(AppTheme.glassBlur),
      backgroundColor: Color.rgba(251, 251, 250, 0.8),
    ),
  ];
}