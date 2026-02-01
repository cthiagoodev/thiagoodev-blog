import 'package:blog/core/constants/theme.dart';
import 'package:blog/modules/components/badge.dart';
import 'package:jaspr/dom.dart';
import 'package:jaspr/jaspr.dart';
import 'package:jaspr_lucide/jaspr_lucide.dart' as jl;

class FeaturedPost extends StatelessComponent {
  @override
  Component build(BuildContext context) {
    return article(classes: 'featured-post', [
      div(classes: 'featured-image-container', [
        img(
          src: 'https://images.unsplash.com/photo-1498050108023-c5249f4df085?auto=format&fit=crop&w=1200&q=80',
          alt: 'Featured Post',
          classes: 'featured-image',
        ),
      ]),
      div(classes: 'featured-content', [
        div(classes: 'meta-tags', [
          Badge(label: 'Destaque', variant: BadgeVariant.primary),
          Badge(label: '8 min leitura', variant: BadgeVariant.neutral),
        ]),
        h2(classes: 'featured-title', [.text('Dominando a Arquitetura Limpa no Flutter: Guia Definitivo 2026')]),
        p(classes: 'featured-excerpt', [
          .text(
            'Descubra como estruturar aplicações escaláveis separando responsabilidades, injetando dependências e mantendo seu código testável do início ao fim.',
          ),
        ]),
        a(href: '/post/slug-do-post', classes: 'read-more', [
          .text('Ler Artigo Completo'),
          jl.ArrowRight(width: 16.px, height: 16.px),
        ]),
      ]),
    ]);
  }

  @css
  static List<StyleRule> get styles => [
    css('.featured-post').styles(
      display: Display.grid,
      padding: Padding.all(2.5.rem),
      radius: AppRadius.lg,
      shadow: AppShadows.card,
      transition: const Transition('all', duration: Duration(milliseconds: 300)),
      alignItems: AlignItems.center,
      gridTemplate: const GridTemplate(
        columns: GridTracks([
          GridTrack(TrackSize.fr(1)),
          GridTrack(TrackSize.fr(1)),
        ]),
      ),
      gap: Gap(column: 3.rem),
      backgroundColor: AppColors.white,
    ),
    css('.featured-post:hover').styles(
      shadow: AppShadows.cardHover,
      transform: Transform.translate(y: (-4).px),
    ),
    css('.featured-image-container').styles(
      width: 100.percent,
      height: 100.percent,
      aspectRatio: const AspectRatio(16, 10),
      radius: AppRadius.md,
      overflow: Overflow.hidden,
    ),
    css('.featured-image').styles(
      width: 100.percent,
      height: 100.percent,
      transition: const Transition('transform', duration: Duration(milliseconds: 500)),
      raw: {'object-fit': 'cover'},
    ),
    css('.featured-post:hover .featured-image').styles(
      transform: Transform.scale(1.05),
    ),
    css('.featured-content').styles(
      display: Display.flex,
      flexDirection: FlexDirection.column,
      alignItems: AlignItems.start,
      gap: Gap(row: 1.5.rem),
    ),
    css('.meta-tags').styles(
      display: Display.flex,
      gap: Gap(column: 0.75.rem),
    ),
    css('.featured-title').styles(
      margin: Margin.zero,
      color: AppColors.foreground,
      fontSize: AppFontSizes.h1,
      fontWeight: FontWeight.w800,
      lineHeight: AppLineHeights.tight,
    ),
    css('.featured-excerpt').styles(
      margin: Margin.zero,
      color: AppColors.neutral,
      fontSize: AppFontSizes.bodyLg,
      lineHeight: AppLineHeights.relaxed,
    ),
    css('.read-more').styles(
      display: Display.flex,
      alignItems: AlignItems.center,
      gap: Gap(column: 0.5.rem),
      color: AppColors.primary,
      fontSize: AppFontSizes.body,
      fontWeight: FontWeight.w600,
      textDecoration: TextDecoration.none,
    ),
    css('.read-more:hover').styles(
      textDecoration: TextDecoration(line: .underline),
    ),
    css('@media (max-width: 768px)').styles(
      raw: {
        'grid-template-columns': '1fr',
        'padding': '1.5rem',
        'gap': '2rem',
      },
    ),
    css('@media (max-width: 768px) .featured-title').styles(
      fontSize: 1.75.rem,
    ),
  ];
}
