<template>
    <b-container fluid>
        <b-row>
            <b-col sm="12">
                <table striped hover class="uiGrid table table-hover rule-table">
                    <thead>
                    <tr>
                        <th class="rule-desc-col">Description</th>
                        <th class="rule-name-col">Title</th>
                        <th class="rule-price-col">score</th>
                        <th class="rule-area-col">Domain</th>
                        <th class="rule-enable-col">Enabled</th>
                        <th class="rule-action-col">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="rule in rules">

                        <td class="rule-desc-col"><div v-if="editedrule.id !== rule.id">{{rule.description}}</div>
                            <input type="text" v-if="editedrule.id === rule.id" v-model="rule.description"style="width: 130px;min-width: 98%;">
                        </td>
                        <td> <div v-if="editedrule.id !== rule.id">{{rule.title}} </div>
                            <input type="text" v-if="editedrule.id === rule.id" class="rule-title-col" v-model="rule.title"style="width: 130px; min-width: 98%;">


                        </td>
                        <td><div v-if="editedrule.id !== rule.id">{{rule.score}}</div>
                            <input  class="rule-needed-score-col" type="text" v-if="editedrule.id === rule.id" v-model="rule.score">
                        </td>
                        <td style="max-width: 115px;">
                            <div v-if="editedrule.id !== rule.id && rule.domainDTO != null">{{rule.domainDTO.title}}</div>
                            <select v-if="editedrule.id === rule.id" v-model="rule.domainDTO"  style="max-width: 115px;margin: 0px auto;height: 35px;" required>
                                <option :value="null" disabled style="max-width: 115px;">Select Your Domain</option>
                                <option v-for="option in domains" v-bind:value="option" style="max-width: 115px">
                                    {{ option.title }}
                                </option>
                            </select>
                        <td>
                            <div v-if="editedrule.id === rule.id">
                                <label class="switch">
                                    <input type="checkbox" v-model="rule.enabled">
                                    <span class="slider round"></span>
                                    <span class="absolute-no">NO</span>
                                </label>
                            </div>
                            <div v-else>
                                <label class="switch" v-on:click ="rule.enabled = !rule.enabled">
                                    <input type="checkbox" v-model="rule.enabled">
                                    <span class="slider round"></span>
                                    <span class="absolute-no">NO</span>
                                </label>
                            </div>
                        </td>
                        <td class="center actionContainer">
                            <a href="#" v-if="editedrule.id !== rule.id" v-on:click.stop="onEdit(rule)" data-placement="bottom" rel="tooltip" class="actionIcon"
                               data-original-title="Edit" v-b-tooltip.hover title="Edit">
                                <i class="uiIconEdit uiIconLightGray"></i></a>
                            <a href="#" v-if="editedrule.id !== rule.id" v-on:click.prevent.stop="onRemove(rule.id,rule.title)" data-placement="bottom" rel="tooltip" class="actionIcon"
                               data-original-title="Supprimer" v-b-tooltip.hover title="Supprimer">
                                <i class="uiIconDelete uiIconLightGray"></i></a>
                            <a href="#" v-if="editedrule.id === rule.id" v-on:click.stop="onSave(rule)" data-placement="bottom" rel="tooltip" class="actionIcon"
                               data-original-title="Edit" v-b-tooltip.hover title="Save">
                                <i class="uiIconSave uiIconLightGray"></i></a>
                            <a href="#" v-if="editedrule.id === rule.id" v-on:click.stop="onCancel(rule)" data-placement="bottom" rel="tooltip" class="actionIcon"
                               data-original-title="Cancel" v-b-tooltip.hover title="Cancel">
                                <i class="uiIcon uiIconClose uiIconBlue"></i></a>
                        </td>
                    </tr>
                    <tr v-if="!rules.length">
                        <td colspan="6" class="p-y-3 text-xs-center" style="cursor: auto; background-color:white;">
                            <strong>You should add some rules!</strong>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </b-col>
        </b-row>
    </b-container>
</template>
<script>
    import Vue from 'vue'
    import BootstrapVue from 'bootstrap-vue'
    import 'bootstrap/dist/css/bootstrap.css'
    import 'bootstrap-vue/dist/bootstrap-vue.css'
    import moment from 'moment'
    Vue.use(BootstrapVue);
    Vue.prototype.moment = moment
    export default {
        props: ['rules','domains'],
        data() {
            return {
                formErrors: {},
                editedrule : {},
                isEnabled: false
            }
        },
        methods: {
            onEdit(rule) {
                this.rule=rule;
                this.editedrule=rule;
            },
            onSave(rule) {
                this.$emit('save', rule);
                this.editedrule= {};
            },
            onCancel(rule) {
                this.editedrule= {};
            },
            onRemove(id, title) {
                this.$emit('remove', id, title)
            }
        }
    }
</script>

<style scoped>
    .container-fluid {
        display: table;
    }
    .table{
        position: relative;
        border-radius: 3px;
        background: #fff;
        margin-bottom: 20px;
        width: 96%;
        box-shadow: 0 1px 1px rgba(0,0,0,.1);
        margin: 14px auto 0;
        margin-bottom: 30px;
    }
    .uiGrid.table tr td {
        padding: 5px;
        vertical-align: inherit;
    }
    .table thead th{font-size: 0.9em;}
    .table td, .table th{
        padding: 8px;
        line-height: 1.42857143;
        vertical-align: top;
        text-align:center;
        border: none;
    }
    .table-hover tbody tr:hover{
        cursor: pointer;
    }
    /*edit Mode */
    td input {
        max-width: min-content;
    }
    input[type="text"] {
        height: 35px;
        margin: auto;
    }
    td.rule-needed-score-col input {
        width: 60px;
        text-align: center;
    }
    td select {
        word-wrap: normal;
        border: Solid 2px #e1e8ee;
        border-radius: 5px;
        margin: auto;
        outline: none;
    }
    input.rule-desc-col {
        min-width: 98%;
    }
    /* switch test */
    .switch {
        position: relative;
        display: inline-block;
        width: 185px;
        height: 66px;
        zoom: 30%;
        top: 0.4rem;
    }
    .switch input {display:none;}
    .slider {
        position: absolute;
        cursor: pointer;
        overflow: hidden;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: #f2f2f2;
        -webkit-transition: .4s;
        transition: .4s;
    }
    .slider:before {
        position: absolute;
        z-index: 2;
        content: "";
        height: 45px;
        width: 45px;
        left: 10px;
        bottom: 11px;
        background-color: darkgrey;
        -webkit-box-shadow: 0 2px 5px rgba(0, 0, 0, 0.22);
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.22);
        -webkit-transition: .4s;
        transition: all 0.4s ease-in-out;
    }
    .slider:after {
        position: absolute;
        left: 0;
        z-index: 1;
        content: "YES";
        font-size: 37px;
        text-align: left !important;
        line-height: 65px;
        padding-left: 0;
        width: 185px;
        height: 66px !important;
        color: #f9f9f9;
        background-color: #477ab3;
        background-image: -moz-linear-gradient(top, #578dc9, #2f5e92);
        background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#578dc9), to(#2f5e92));
        background-image: -webkit-linear-gradient(top, #578dc9, #2f5e92);
        background-image: -o-linear-gradient(top, #578dc9, #2f5e92);
        background-image: linear-gradient(to bottom, #578dc9, #2f5e92);
        background-repeat: repeat-x;
        filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff578dc9', endColorstr='#ff2f5e92', GradientType=0);
        -webkit-box-shadow: inset 0px 3px 5px #224469;
        -moz-box-shadow: inset 0px 3px 5px #224469;
        box-shadow: inset 0px 3px 5px #224469;
        -webkit-border-top-left-radius: 9px;
        -moz-border-radius-topleft: 9px;
        border-top-left-radius: 9px;
        -webkit-border-bottom-left-radius: 9px;
        -moz-border-radius-bottomleft: 9px;
        border-bottom-left-radius: 9px;
        height: 57px;
        border-radius: 100px;
        background-color: #578dc9;
        -webkit-transform: translateX(-190px);
        -ms-transform: translateX(-190px);
        transform: translateX(-190px);
        transition: all 0.4s ease-in-out;
    }
    input:checked + .slider:after {
        -webkit-transform: translateX(0px);
        -ms-transform: translateX(0px);
        transform: translateX(0px);
        padding-left: 25px;
    }
    input:checked + .slider:before {
        background-color: #fff;
    }
    input:checked + .slider:before {
        -webkit-transform: translateX(115px);
        -ms-transform: translateX(115px);
        transform: translateX(115px);
    }
    /* Rounded sliders */
    .slider.round {
        border-radius: 100px;
    }
    .slider.round:before {
        border-radius: 50%;
    }
    .absolute-no {
        position: absolute;
        left: 0;
        color: darkgrey;
        text-align: right !important;
        font-size: 45px;
        width: calc(100% - 25px);
        line-height: 70px;
        cursor: pointer;
    }
    input.rule-needed-score-col{
        max-width: 60px;
        text-align: center;
    }
    i.uiIconClose.uiIconBlue {
        zoom: 163%;
        vertical-align: super;
        opacity: 1;
        line-height: inherit;
    }
    i.uiIconSave.uiIconLightGray {
        left: -4px;
    }
</style>