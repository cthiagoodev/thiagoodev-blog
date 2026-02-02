import 'package:blog/core/constants/theme.dart';
import 'package:blog/presentation/global_components/buttons.dart';
import 'package:jaspr/dom.dart';
import 'package:jaspr/jaspr.dart';
import 'package:jaspr_lucide/jaspr_lucide.dart' as jl;

final class Nav extends StatelessComponent {
  static const String homeLink = "/";
  static const String portfolioLink = "https://thiagoodev.com.br";
  static const String aboutLink = "/sobre";

  @override
  Component build(BuildContext context) {
    return nav([
      div(classes: 'nav-brand', [
        a(href: '/', [
          jl.Code(width: 24.px, height: 24.px),
          span([.text('thiagoodev')]),
          b([.text('blog')]),
        ]),
      ]),

      ul([
        li([
          a(
            href: homeLink,
            [jl.BookOpen(width: 18.px, height: 18.px), .text('Blog')],
          ),
        ]),

        li([
          a(
            href: portfolioLink,
            target: .blank,
            [jl.Layers(width: 18.px, height: 18.px), .text('Portfólio')],
          ),
        ]),

        li([
          a(
            href: aboutLink,
            [jl.User(width: 18.px, height: 18.px), .text('Sobre')],
          ),
        ]),
      ]),

      div(classes: 'nav-actions', [
        const Button.ghost(
          label: "Entrar",
        ),
        const Button.primary(
          label: "Criar Conta",
        ),
      ]),
    ]);
  }

  @css
  static List<StyleRule> get styles => [
    css('nav').styles(
      display: Display.flex,
      maxWidth: AppTheme.containerLg,
      padding: Padding.symmetric(vertical: 1.rem, horizontal: 1.5.rem),
      margin: Margin.symmetric(horizontal: .auto),
      justifyContent: JustifyContent.spaceBetween,
      alignItems: AlignItems.center,
    ),

    css('.nav-brand a').styles(
      display: Display.flex,
      alignItems: AlignItems.center,
      gap: Gap(column: 0.5.rem),
      color: AppColors.textSecondary,
      fontSize: 1.25.rem,
      fontWeight: FontWeight.w700,
      textDecoration: .none,
      lineHeight: Unit.zero,
    ),

    css('.nav-brand b').styles(
      color: AppColors.primaryDeep,
      fontWeight: FontWeight.w400,
      fontStyle: FontStyle.italic,
    ),

    css('nav ul').styles(
      display: Display.flex,
      padding: .zero,
      margin: .zero,
      gap: Gap(column: 2.rem),
      listStyle: .none,
    ),

    css('nav ul a').styles(
      display: Display.flex,
      transition: const Transition('all', duration: Duration(milliseconds: 200)),
      alignItems: AlignItems.center,
      gap: Gap(column: 0.5.rem),
      color: AppColors.textSecondary,
      fontSize: AppFontSizes.bodySm,
      fontWeight: FontWeight.w500,
      textDecoration: .none,
      lineHeight: Unit.zero,
    ),

    css('nav ul a:hover').styles(
      transform: Transform.translate(y: (-1).px),
      color: AppColors.primary,
    ),

    css('.nav-actions').styles(
      display: Display.flex,
      flexDirection: FlexDirection.row,
      alignItems: .center,
      gap: Gap(column: 1.rem),
    ),
  ];
}