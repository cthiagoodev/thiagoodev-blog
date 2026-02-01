import 'package:blog/core/constants/theme.dart';
import 'package:blog/modules/components/section_title.dart';
import 'package:blog/modules/posts/components/post_card.dart';
import 'package:jaspr/dom.dart';
import 'package:jaspr/jaspr.dart';
import 'package:jaspr_lucide/jaspr_lucide.dart' as jl;

class WeeklyCarousel extends StatelessComponent {
  @override
  Component build(BuildContext context) {
    return section(classes: 'weekly-carousel', [
      div(classes: 'carousel-header', [
        const SectionTitle(title: 'Destaques da Semana'),
        div(classes: 'carousel-nav', [
          button(classes: 'nav-btn', [jl.ChevronLeft(width: 20.px, height: 20.px)]),
          button(classes: 'nav-btn', [jl.ChevronRight(width: 20.px, height: 20.px)]),
        ]),
      ]),
      div(classes: 'carousel-grid', [
        const PostCard(
          title: 'O Futuro do State Management no Dart',
          category: 'Backend',
          date: '2 Fev 2026',
          imageSrc: 'https://images.unsplash.com/photo-1555099962-4199c345e5dd?auto=format&fit=crop&w=800&q=80',
        ),
        const PostCard(
          title: 'Micro-frontends com Jaspr: Vale a pena?',
          category: 'Web',
          date: '1 Fev 2026',
          imageSrc: 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?auto=format&fit=crop&w=800&q=80',
        ),
        const PostCard(
          title: 'UX Design para Desenvolvedores Mobile',
          category: 'Design',
          date: '28 Jan 2026',
          imageSrc: 'https://images.unsplash.com/photo-1586717791821-3f44a5638d48?auto=format&fit=crop&w=800&q=80',
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
