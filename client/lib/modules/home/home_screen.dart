import 'package:blog/core/constants/theme.dart';
import 'package:blog/modules/posts/components/featured_post.dart';
import 'package:blog/modules/posts/components/weekly_carousel.dart';
import 'package:jaspr/dom.dart';
import 'package:jaspr/jaspr.dart';

final class HomeScreen extends StatelessComponent {
  @override
  Component build(BuildContext context) {
    return section(classes: 'home-container', [
      FeaturedPost(),
      WeeklyCarousel(),
    ]);
  }

  @css
  static List<StyleRule> get styles => [
    css('.home-container').styles(
      display: Display.flex,
      width: 100.percent,
      maxWidth: AppTheme.containerLg,
      padding: Padding.symmetric(vertical: 3.rem, horizontal: 1.5.rem),
      margin: Margin.symmetric(horizontal: .auto),
      flexDirection: FlexDirection.column,
      gap: Gap(row: 4.rem),
    ),
  ];
}