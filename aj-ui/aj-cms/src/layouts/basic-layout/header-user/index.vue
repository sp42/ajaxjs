<template>
    <span class="i-layout-header-trigger i-layout-header-trigger-min">
        <Dropdown :trigger="isMobile ? 'click' : 'hover'" class="i-layout-header-user" :class="{ 'i-layout-header-user-mobile': isMobile }" @on-click="handleClick">
            <Avatar size="small" :src="info.avatar" v-if="info.avatar" />
            <span class="i-layout-header-user-name" v-if="!isMobile">{{ info.name }}</span>
            <DropdownMenu slot="list">
                <!--<i-link to="/setting/user">-->
                    <!--<DropdownItem>-->
                        <!--<Icon type="ios-contact-outline" />-->
                        <!--<span>{{ $t('basicLayout.user.center') }}</span>-->
                    <!--</DropdownItem>-->
                <!--</i-link>-->
                <!--<i-link to="/setting/account">-->
                    <!--<DropdownItem>-->
                        <!--<Icon type="ios-settings-outline" />-->
                        <!--<span>{{ $t('basicLayout.user.setting') }}</span>-->
                    <!--</DropdownItem>-->
                <!--</i-link>-->
                <DropdownItem divided name="logout">
                    <Icon type="ios-log-out" />
                    <span>{{ $t('basicLayout.user.logOut') }}</span>
                </DropdownItem>
            </DropdownMenu>
        </Dropdown>
    </span>
</template>
<script>
    import { mapState } from 'vuex';
import { Session } from '@bingo/core';

    export default {
        name: 'iHeaderUser',
        computed: {
            ...mapState('admin/user', [
                'info'
            ]),
            ...mapState('admin/layout', [
                'isMobile',
                'logoutConfirm'
            ])
        },
        methods: {
            handleClick (name) {
                if (name === 'logout') {
                    this.$Modal.confirm({
                        title: '退出确认',
                        content: '是否退出当前系统？',
                        onOk: () => {
                            Session.doLogout();
                        }
                    });
                }
            }
        }
    }
</script>
