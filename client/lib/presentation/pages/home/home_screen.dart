import 'package:blog/presentation/global_components/app_container.dart';
import 'package:blog/presentation/pages/posts/components/featured_post.dart';
import 'package:blog/presentation/pages/posts/components/weekly_carousel.dart';
import 'package:jaspr/dom.dart';
import 'package:jaspr/jaspr.dart';

final class HomeScreen extends StatelessComponent {
  @override
  Component build(BuildContext context) {
    return AppContainer(
      customClass: 'home-wrapper',
      children: [
        FeaturedPost(),
        WeeklyCarousel(),
      ],
    );
  }

  @css
  static List<StyleRule> get styles => [
    css('.home-wrapper').styles(
      display: Display.flex,
      padding: Padding.only(top: 3.rem, bottom: 3.rem),
      flexDirection: FlexDirection.column,
      gap: Gap(row: 4.rem),
    ),
  ];
}