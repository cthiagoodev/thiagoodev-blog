import 'package:blog/core/constants/theme.dart';
import 'package:jaspr/dom.dart';
import 'package:jaspr/jaspr.dart';

class AppContainer extends StatelessComponent {
  final List<Component> children;
  final String? customClass;

  const AppContainer({required this.children, this.customClass});

  @override
  Component build(BuildContext context) {
    return div(
      classes: 'app-container ${customClass ?? ''}',
      children,
    );
  }

  @css
  static List<StyleRule> get styles => [
    css('.app-container').styles(
      width: 100.percent,
      maxWidth: AppTheme.containerLg,
      padding: Padding.symmetric(horizontal: 1.5.rem),
      margin: Margin.symmetric(horizontal: .auto),
      boxSizing: BoxSizing.borderBox,
    ),
    css('@media (max-width: 768px)').styles(
      raw: {'padding': '0 1rem'},
    ),
  ];
}