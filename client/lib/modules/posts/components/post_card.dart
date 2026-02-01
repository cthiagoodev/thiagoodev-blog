import 'package:blog/core/constants/theme.dart';
import 'package:blog/modules/components/app_card.dart';
import 'package:blog/modules/components/badge.dart';
import 'package:blog/modules/components/buttons.dart';
import 'package:jaspr/dom.dart';
import 'package:jaspr/jaspr.dart';

class PostCard extends StatelessComponent {
  final String title;
  final String category;
  final String? imageSrc;
  final String date;

  const PostCard({
    required this.title,
    required this.category,
    this.imageSrc,
    required this.date,
  });

  @override
  Component build(BuildContext context) {
    final hasImage = imageSrc != null && imageSrc!.isNotEmpty;

    return AppCard(
      href: '#',
      children: [
        if (hasImage)
          div(classes: 'card-media', [
            img(src: imageSrc!, classes: 'card-img', alt: title),
          ]),
        div(classes: 'card-body', [
          div(classes: 'card-meta', [
            Badge(label: category, variant: BadgeVariant.primary),
            span(classes: 'card-date', [.text(date)]),
          ]),
          h3(classes: 'card-title', [.text(title)]),
          div(classes: 'card-footer', [
            const LinkButton(label: 'Ler mais'),
          ]),
        ]),
      ],
    );
  }

  @css
  static List<StyleRule> get styles => [
    css('.card-media').styles(
      width: 100.percent,
      aspectRatio: const AspectRatio(16, 10),
      overflow: Overflow.hidden,
      flex: Flex(grow: 0, shrink: 0),
      position: Position.relative(),
    ),
    css('.card-img').styles(
      display: Display.block,
      width: 100.percent,
      height: 100.percent,
      raw: {'object-fit': 'cover'},
      transition: const Transition('transform', duration: Duration(milliseconds: 500)),
    ),
    css('.app-card:hover .card-img').styles(
      transform: Transform.scale(1.05),
    ),
    css('.card-body').styles(
      display: Display.flex,
      flexDirection: FlexDirection.column,
      padding: Padding.all(1.5.rem),
      gap: Gap(row: 1.rem),
      flex: Flex(grow: 1),
      width: 100.percent,
      boxSizing: BoxSizing.borderBox,
    ),
    css('.card-meta').styles(
      display: Display.flex,
      justifyContent: JustifyContent.spaceBetween,
      alignItems: AlignItems.center,
    ),
    css('.card-date').styles(
      color: AppColors.neutral,
      fontSize: 0.8.rem,
      fontWeight: FontWeight.w500,
    ),
    css('.card-title').styles(
      margin: Margin.zero,
      color: AppColors.foreground,
      fontSize: 1.25.rem,
      fontWeight: FontWeight.w700,
      lineHeight: 1.3.em,
    ),
    css('.card-footer').styles(
      display: Display.flex,
      margin: .only(top: .auto),
      alignItems: AlignItems.center,
    ),
  ];
}
