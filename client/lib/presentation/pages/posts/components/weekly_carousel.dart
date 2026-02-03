import 'package:blog/core/constants/theme.dart';
import 'package:blog/core/di/injection.dart';
import 'package:blog/domain/models/publication.dart';
import 'package:blog/domain/usecases/get_current_week_publications_usecase.dart';
import 'package:blog/presentation/global_components/section_title.dart';
import 'package:blog/presentation/pages/posts/components/post_card.dart';
import 'package:jaspr/dom.dart';
import 'package:jaspr/server.dart';
import 'package:jaspr_lucide/jaspr_lucide.dart' as jl;

class WeeklyCarousel extends AsyncStatelessComponent {
  const WeeklyCarousel({super.key});

  @override
  Future<Component> build(BuildContext context) async {
    final GetCurrentWeekPublicationsUseCase getCurrentWeekPublicationsUseCase = injection();
    final List<Publication> publications = await getCurrentWeekPublicationsUseCase();

    if (publications.isEmpty) {
      return div([]);
    }

    return section(classes: 'weekly-carousel', [
      div(classes: 'carousel-header', [
        const SectionTitle(title: 'Destaques da Semana'),
        div(classes: 'carousel-nav', [
          button(classes: 'nav-btn', [jl.ChevronLeft(width: 20.px, height: 20.px)]),
          button(classes: 'nav-btn', [jl.ChevronRight(width: 20.px, height: 20.px)]),
        ]),
      ]),
      div(classes: 'carousel-grid', [
        for (final publication in publications)
          PostCard(
            title: publication.title,
            category: publication.tags.isNotEmpty ? publication.tags.first : 'Geral',
            date: publication.createdAt.toString(),
            imageSrc: publication.image ?? '',
          ),
      ]),
    ]);
  }

  @css
  static List<StyleRule> get styles => [
    css('.weekly-carousel').styles(
      width: 100.percent,
    ),
    css('.carousel-header').styles(
      display: Display.flex,
      margin: Margin.only(bottom: 1.rem),
      justifyContent: JustifyContent.spaceBetween,
      alignItems: AlignItems.start,
    ),
    css('.carousel-nav').styles(
      display: Display.flex,
      margin: Margin.only(top: 0.5.rem),
      gap: Gap(column: 0.75.rem),
    ),
    css('.nav-btn').styles(
      display: Display.flex,
      width: 2.5.rem,
      height: 2.5.rem,
      border: Border.all(width: 1.px, color: AppColors.border),
      radius: .circular(50.percent),
      cursor: Cursor.pointer,
      transition: const Transition('all', duration: Duration(milliseconds: 200)),
      justifyContent: JustifyContent.center,
      alignItems: AlignItems.center,
      color: AppColors.foreground,
      backgroundColor: Colors.transparent,
    ),
    css('.nav-btn:hover').styles(
      border: .all(color: AppColors.primary),
      color: AppColors.white,
      backgroundColor: AppColors.primary,
    ),
    css('.carousel-grid').styles(
      display: Display.grid,
      gridTemplate: const GridTemplate(
        columns: GridTracks([
          GridTrack(TrackSize.fr(1)),
          GridTrack(TrackSize.fr(1)),
          GridTrack(TrackSize.fr(1)),
        ]),
      ),
      gap: Gap(column: 2.rem),
    ),
    css('@media (max-width: 768px)').styles(
      raw: {'grid-template-columns': '1fr'},
    ),
  ];
}