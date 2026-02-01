import 'package:blog/core/constants/theme.dart';
import 'package:blog/modules/components/badge.dart';
import 'package:jaspr/dom.dart';
import 'package:jaspr/jaspr.dart';
import 'package:jaspr_lucide/jaspr_lucide.dart' as jl;

class PostCard extends StatelessComponent {
  final String title;
  final String category;
  final String imageSrc;
  final String date;

  const PostCard({
    required this.title,
    required this.category,
    required this.imageSrc,
    required this.date,
  });

  @override
  Component build(BuildContext context) {
    return a(href: '#', classes: 'post-card', [
      div(classes: 'card-image-wrap', [
        img(src: imageSrc, classes: 'card-image', alt: title),
      ]),
      div(classes: 'card-content', [
        div(classes: 'card-meta', [
          Badge(label: category, variant: BadgeVariant.primary),
          span(classes: 'card-date', [.text(date)]),
        ]),
        h3(classes: 'card-title', [.text(title)]),
        div(classes: 'card-footer', [
          .text('Ler mais'),
          jl.ArrowRight(width: 14.px, height: 14.px),
        ]),
      ]),
    ]);
  }

  @css
  static List<StyleRule> get styles => [
    css('.post-card').styles(
      display: Display.flex,
      height: 100.percent,
      radius: AppRadius.lg,
      overflow: Overflow.hidden,
      shadow: BoxShadow(
        offsetX: 0.px,
        offsetY: 2.px,
        blur: 10.px,
        color: Color.rgba(0, 0, 0, 0.03),
      ),
      transition: const Transition('all', duration: Duration(milliseconds: 300)),
      flexDirection: FlexDirection.column,
      textDecoration: TextDecoration.none,
      backgroundColor: AppColors.white,
    ),
    css('.post-card:hover').styles(
      shadow: AppShadows.cardHover,
      transform: Transform.translate(y: (-4).px),
    ),
    css('.card-image-wrap').styles(
      width: 100.percent,
      aspectRatio: const AspectRatio(4, 3),
      overflow: Overflow.hidden,
    ),
    css('.card-image').styles(
      width: 100.percent,
      height: 100.percent,
      transition: const Transition('transform', duration: Duration(milliseconds: 500)),
      raw: {'object-fit': 'cover'},
    ),
    css('.post-card:hover .card-image').styles(
      transform: Transform.scale(1.05),
    ),
    css('.card-content').styles(
      display: Display.flex,
      padding: Padding.all(1.5.rem),
      flexDirection: FlexDirection.column,
      gap: Gap(row: 1.rem),
      flex: Flex(grow: 1),
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
      lineHeight: 1.4.rem,
    ),
    css('.card-footer').styles(
      display: Display.flex,
      margin: Margin.only(top: .auto),
      alignItems: AlignItems.center,
      gap: Gap(column: 0.4.rem),
      color: AppColors.primary,
      fontSize: 0.9.rem,
      fontWeight: FontWeight.w600,
    ),
  ];
}
