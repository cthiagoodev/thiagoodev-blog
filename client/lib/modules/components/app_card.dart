import 'package:blog/core/constants/theme.dart';
import 'package:jaspr/dom.dart';
import 'package:jaspr/jaspr.dart';

class AppCard extends StatelessComponent {
  final List<Component> children;
  final String? href;
  final String? customClass;
  final bool interactable;

  const AppCard({
    required this.children,
    this.href,
    this.customClass,
    this.interactable = true,
  });

  @override
  Component build(BuildContext context) {
    final classNames = 'app-card ${interactable ? 'interactable' : ''} ${customClass ?? ''}';

    if (href != null) {
      return a(href: href!, classes: classNames, children);
    }

    return div(classes: classNames, children);
  }

  @css
  static List<StyleRule> get styles => [
    css('.app-card').styles(
      display: Display.flex,
      width: 100.percent,
      height: 100.percent,
      boxSizing: BoxSizing.borderBox,
      border: Border.none,
      radius: AppRadius.lg,
      overflow: Overflow.hidden,
      shadow: AppShadows.card,
      transition: const Transition('all', duration: Duration(milliseconds: 300)),
      flexDirection: FlexDirection.column,
      justifyContent: JustifyContent.start,
      alignItems: AlignItems.stretch,
      color: const Color('inherit'),
      textDecoration: TextDecoration.none,
      backgroundColor: AppColors.white,
    ),
    css('.app-card.interactable:hover').styles(
      shadow: AppShadows.cardHover,
      transform: Transform.translate(y: (-4).px),
    ),
  ];
}
