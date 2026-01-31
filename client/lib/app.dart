import 'package:blog/modules/home/home_screen.dart';
import 'package:jaspr/dom.dart';
import 'package:jaspr/jaspr.dart';
import 'package:jaspr_router/jaspr_router.dart';

import 'core/constants/theme.dart';

class App extends StatelessComponent {
  const App({super.key});

  @override
  Component build(BuildContext context) {
    return div(classes: 'main', [
      Router(routes: [
        Route(path: '/', name: "Home", builder: (context, state) => HomeScreen()),
      ]),
    ]);
  }

  @css
  static List<StyleRule> get styles => [
    css('.main').styles(
      display: .flex,
      minHeight: 100.vh,
      flexDirection: .column,
    ),
    css('section').styles(
      width: 100.percent,
      maxWidth: .variable('--container-lg'),
      padding: Padding.all(AppSpacing.md),
      margin: Margin.symmetric(horizontal: .auto),
      flex: Flex(grow: 1),
    )
  ];
}
