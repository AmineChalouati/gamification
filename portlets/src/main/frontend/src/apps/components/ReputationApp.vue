<template>
    <div id="user-reputation-container" class="uiBox">
        <reputation-points></reputation-points>
        <reputation-badges></reputation-badges>
    </div>
</template>
<script>

    import ReputationBadges from './reputation/ReputationBadges'
    import ReputationPoints from './reputation/ReputationPoints'

    export default {
        components: {
            ReputationBadges,
            ReputationPoints

        },
       data: () => ({
           isGamificationEnabled: false,
            tabs: TABS,
            currentTab: 'MyPoints',
        }),
        methods:{
            handleClick(newTab) {
                this.currentTab = newTab;
            },
            maximize() {
                window.location.href = `${eXo.env.portal.context}/${eXo.env.portal.portalName}/achievements/${eXo.env.portal.profileOwner}`;

            },

            initMenuApp() {
                if (!this.isGamificationEnabled ) {
                    return;
                }
                this.$nextTick(() => {
                    if ($('#myGamificationTab').length) {
                        return;
                    }
                    if (!$('.userNavigation .item').length) {
                        setTimeout(this.initMenuApp, 500);
                        return;
                    }
                    $('.userNavigation').append(` \
          <li id='myGamificationTab' class='item${this.isMaximized ? ' active' : ''}'> \
            <a href='${eXo.env.portal.context}/${eXo.env.portal.portalName}/achievements/${eXo.env.portal.profileOwner}'>
              <div class='uiIconAppGamification uiIconDefaultApp' /> \
              <span class='tabName'>My Achievements</span> \
            </a> \
          </li>`);
                    $(window).trigger('resize');
                });
            },
        },
        created() {
            if ((!eXo && eXo.env) || !eXo.env.portal || !eXo.env.portal.userName || !eXo.env.portal.userName.length) {
                this.isGamificationEnabled = false;
                return;
            }
            if (eXo.env.portal.profileOwner && eXo.env.portal.profileOwner !== eXo.env.portal.userName) {
                this.isGamificationEnabled = false;
                return;
            } else {
                this.isGamificationEnabled = true;
                this.initMenuApp();
            }

        }


    }
</script>


<style>
    @font-face {
        font-family: 'open_sanssemibold';
        src: url('/gamification-portlets/skin/fonts/opensans-semibold-webfont.woff2') format('woff2'),
        url('/gamification-portlets/skin/fonts/opensans-semibold-webfont.woff') format('woff');
        font-weight: normal;
        font-style: normal;
    }
    @font-face {
        font-family: 'open_sansbold';
        src: url('/gamification-portlets/skin/fonts/opensans-bold-webfont.woff2') format('woff2'),
        url('/gamification-portlets/skin/fonts/opensans-bold-webfont.woff') format('woff');
        font-weight: normal;
        font-style: normal;
    }
    @font-face {
        font-family: 'open_sansregular';
        src: url('/gamification-portlets/skin/fonts/opensans-regular-webfont.woff2') format('woff2'),
        url('/gamification-portlets/skin/fonts/opensans-regular-webfont.woff') format('woff');
        font-weight: normal;
        font-style: normal;
    }
    #user-reputation-container h4 {
        color: #4d5466 !important;
        font-family: 'open_sanssemibold';
        font-size: 17px;
    }
    #user-reputation-container h5 {
        color: #212529 !important;
        font-family: 'open_sanssemibold' !important;
        font-size: 20px;
        padding: 15px 10px 0 10px;
        text-align: center;
    }
    .fade:not(.show) {
        opacity: 1 !important;
    }
    .popover .arrow {
        width: auto !important;
        height: auto !important;
        margin: 0 !important;
    }
    .fade {
        opacity: 1 !important;
    }
    .bs-popover-auto[x-placement^=top] .arrow,
    .bs-popover-top .arrow {
        bottom: calc((.5rem + 1px) * -2.1) !important;
    }
    .bs-popover-auto[x-placement^=left] .arrow,
    .bs-popover-left .arrow {
        right: calc((.5rem + 1px) * -2.2) !important;
    }
    .bs-popover-auto[x-placement^=bottom] .arrow,
    .bs-popover-bottom .arrow {
        top: calc((.5rem + 1px) * -2.2) !important;
    }
    .bs-popover-auto[x-placement^=left] .arrow,
    .bs-popover-right .arrow {
        right: calc((.5rem + 1px) * -2.2) !important;
    }
    .no-padding {
        padding: 5px;
    }
    .reputation-badge-container {
        padding-right: 10px !important;
        padding-left: 10px !important;
    }
    .popover {
        max-width: 222px !important;
        width: 200px !important;
        border: none !important;
        top: -10px !important;
    }
    .popover-body {
        text-align: center !important;
        padding: 10px 0px 0px 0 !important;
        line-height: 20px;
        font-family: helvetica;
    }
    .bs-popover-auto[x-placement^=top] .arrow:before,
    .bs-popover-top .arrow:before {
        border-top-color: inherit !important;
    }
    .bs-popover-auto[x-placement^=top] .arrow:after,
    .bs-popover-top .arrow:after {
        bottom: -10px;
        border-top-color: #f0f8fe;
        left: -10px;
    }
    .bs-popover-auto[x-placement^=bottom] .arrow:after,
    .bs-popover-bottom .arrow:after {
        top: -10px;
        border-bottom-color: #f0f8fe;
        left: -10px;
    }
    .img-thumbnail {
        border: 0px !important;
    }
    .user-reputation-container {
        margin-top: 15px;
    }
    .uiIconAppGamification:before {
        content: "\ebdb"!important;
    }
    .uiProfileMenu .userNavigation > .active > a > span, .uiSpaceMenu .spaceMenuTab > .active > a > span {
        color: #578dc9;
    }
    .uiProfileMenu .userNavigation > .active > a, .uiProfileMenu .userNavigation > .active > a:hover, .uiSpaceMenu .spaceMenuTab > .active > a, .uiSpaceMenu .spaceMenuTab > .active > a:hover {
        background: #ffffff;
        background-image: none;
        color: #578dc9;
        border-bottom: 4px solid #578dc9;
        border-radius: 0;
        box-shadow: none;
    }
    .uiProfileMenu .userNavigation > .active > a > div, .uiSpaceMenu .spaceMenuTab > .active > a > i {
        color: #578dc9;
    }


</style>