import 'package:blog/core/constants/theme.dart';
import 'package:blog/core/di/injection.dart';
import 'package:blog/domain/models/publication.dart';
import 'package:blog/domain/usecases/get_featured_publication_usecase.dart';
import 'package:blog/presentation/global_components/app_card.dart';
import 'package:blog/presentation/global_components/badge.dart';
import 'package:blog/presentation/global_components/buttons.dart';
import 'package:jaspr/dom.dart';
import 'package:jaspr/server.dart';

class FeaturedPost extends AsyncStatelessComponent {
  const FeaturedPost({super.key});

  @override
  Future<Component> build(BuildContext context) async {
    final GetFeaturedPublicationUseCase getFeaturedPublicationUseCase = injection();
    final Publication publication = await getFeaturedPublicationUseCase();

    final hasImage = publication.image != null && publication.image!.isNotEmpty;

    return AppCard(
      interactable: false,
      children: [
        div(classes: 'featured-inner ${!hasImage ? 'no-image' : ''}', [
          if (hasImage)
            div(classes: 'featured-image-container', [
              img(
                src: publication.image!,
                alt: publication.title,
                classes: 'featured-image',
              ),
            ]),
          div(classes: 'featured-content', [
            div(classes: 'meta-tags', [
              Badge(label: 'Destaque', variant: .primary),
              if (publication.tags.isNotEmpty)
                Badge(label: publication.tags.first, variant: .neutral),
            ]),
            h2(classes: 'featured-title', [.text(publication.title)]),
            p(classes: 'featured-excerpt', [
              .text(publication.description),
            ]),
            LinkButton(
              label: 'Ler Artigo Completo',
              href: '/post/${publication.slug}',
            ),
          ]),
        ]),
      ],
    );
  }

  @css
  static List<StyleRule> get styles => [
    css('.featured-inner').styles(
      display: Display.grid,
      width: 100.percent,
      padding: Padding.all(2.5.rem),
      boxSizing: BoxSizing.borderBox,
      alignItems: AlignItems.center,
      gridTemplate: const GridTemplate(
        columns: GridTracks([
          GridTrack(TrackSize.fr(1)),
          GridTrack(TrackSize.fr(1)),
        ]),
      ),
      gap: Gap(column: 3.rem),
    ),
    css('.featured-inner.no-image').styles(
      raw: {'grid-template-columns': '1fr'},
    ),
    css('.featured-image-container').styles(
      width: 100.percent,
      height: 100.percent,
      aspectRatio: const AspectRatio(16, 10),
      radius: AppRadius.md,
      overflow: Overflow.hidden,
      shadow: AppShadows.card,
    ),
    css('.featured-image').styles(
      display: Display.block,
      width: 100.percent,
      height: 100.percent,
      transition: const Transition('transform', duration: Duration(milliseconds: 500)),
      raw: {'object-fit': 'cover'},
    ),
    css('.featured-inner:hover .featured-image').styles(
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
      lineHeight: 1.2.em,
    ),
    css('.featured-excerpt').styles(
      margin: Margin.zero,
      color: AppColors.neutral,
      fontSize: AppFontSizes.bodyLg,
      lineHeight: AppLineHeights.relaxed,
    ),
    css('@media (max-width: 768px) .featured-inner').styles(
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