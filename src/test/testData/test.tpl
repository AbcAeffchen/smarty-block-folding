
{function name='getTitle'}
    Some Title Text
{/function}

{block}
    test
{/block}

{tabs id='test'}
    {tab name='t1'}
        {call getTitle}
        <select name="s1" id="s1">
        {foreach $list as $id => $val}
            <option value="{$id}">{$val}</option>
        {/foreach}
        </select>
    {/tab}
{/tabs}
