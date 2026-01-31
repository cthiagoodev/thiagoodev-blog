import 'package:jaspr/dom.dart';
import 'package:jaspr/jaspr.dart';

final class HomeScreen extends StatelessComponent {
  @override
  Component build(BuildContext context) {
    return .fragment([
      .text("Hello World")
    ]);
  }
}